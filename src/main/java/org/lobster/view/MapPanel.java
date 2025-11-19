package org.lobster.view;

import org.lobster.entity.MapBounds;
import org.lobster.entity.MapCoordinate;
import org.lobster.entity.MapPlane;
import org.lobster.interface_adapter.map_view.MapViewController;
import org.lobster.interface_adapter.map_view.MapViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Custom JPanel that renders a map with aircraft positions
 */
public class MapPanel extends JPanel implements PropertyChangeListener {
    
    private final MapViewController controller;
    private final MapViewModel viewModel;
    private BufferedImage worldMapImage;
    
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 400;
    private static final Color BACKGROUND_COLOR = new Color(173, 216, 230); // Light blue
    private static final Color GRID_COLOR = new Color(100, 100, 100, 150); // Darker gray with more opacity for visibility over image
    private static final Color PLANE_COLOR = Color.RED;
    private static final Color PLANE_LABEL_COLOR = Color.BLACK;
    
    public MapPanel(MapViewController controller, MapViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        setBackground(BACKGROUND_COLOR);

        // Listen for view model changes
        viewModel.addPropertyChangeListener(this);
        
        // Load world map image
        loadWorldMapImage();
        
        // Initialize the map
        initializeMap();
    }
    
    private void initializeMap() {
        controller.initializeMap(getWidth() > 0 ? getWidth() : DEFAULT_WIDTH, 
                                getHeight() > 0 ? getHeight() : DEFAULT_HEIGHT);
    }
    
    private void loadWorldMapImage() {
        try {
            InputStream imageStream = getClass().getResourceAsStream("/mercatorworldmap.jpg");
            if (imageStream != null) {
                worldMapImage = ImageIO.read(imageStream);
                System.out.println("World map image loaded successfully");
            } else {
                System.err.println("Could not find mercatorworldmap.jpg in resources");
            }
        } catch (IOException e) {
            System.err.println("Failed to load world map image: " + e.getMessage());
            worldMapImage = null;
        }
    }
    
    /**
     * Update plane positions in world coordinates
     */
    public void updatePlanePositions(List<String> flightNumbers) {
        controller.updateMap(flightNumbers, getWidth(), getHeight());
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Enable anti-aliasing for smoother rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        drawWorldMapBackground(g2d);
        drawMap(g2d);
        drawPlanes(g2d);
        drawStatusMessage(g2d);
        
        g2d.dispose();
    }
    
    private void drawWorldMapBackground(Graphics2D g2d) {
        if (worldMapImage != null) {
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            
            // Draw the image scaled to fit the panel dimensions
            // This will "squish" the image to fit regardless of aspect ratio
            g2d.drawImage(worldMapImage, 0, 0, panelWidth, panelHeight, null);
            
            // Optional: Add a slight transparency overlay to make grid lines more visible
            g2d.setColor(new Color(255, 255, 255, 20)); // Very light white overlay
            g2d.fillRect(0, 0, panelWidth, panelHeight);
        }
    }
    
    private void drawMap(Graphics2D g2d) {
        int width = getWidth();
        int height = getHeight();
        
        g2d.setColor(GRID_COLOR);
        g2d.setStroke(new BasicStroke(1));
        
        // Draw longitude lines (vertical) every 30 degrees
        for (int lon = -180; lon <= 180; lon += 30) {
            double x = (lon + 180.0) / 360.0 * width;
            g2d.drawLine((int)x, 0, (int)x, height);
            
            // Label major longitude lines
            if (lon % 60 == 0 && lon != 0) {
                g2d.setColor(Color.DARK_GRAY);
                g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
                String label = (lon > 0) ? lon + "°E" : Math.abs(lon) + "°W";
                g2d.drawString(label, (int)x + 2, height - 5);
                g2d.setColor(GRID_COLOR);
            }
        }
        
        // Draw latitude lines (horizontal) using Mercator projection
        for (int lat = -80; lat <= 80; lat += 20) {
            double latRad = Math.toRadians(lat);
            double mercatorY = Math.log(Math.tan(Math.PI/4 + latRad/2));
            
            // Convert to screen coordinates using the same projection as MapBounds
            double minLatRad = Math.toRadians(-85);
            double maxLatRad = Math.toRadians(85);
            double minMercatorY = Math.log(Math.tan(Math.PI/4 + minLatRad/2));
            double maxMercatorY = Math.log(Math.tan(Math.PI/4 + maxLatRad/2));
            
            double y = height - ((mercatorY - minMercatorY) / (maxMercatorY - minMercatorY) * height);
            
            g2d.drawLine(0, (int)y, width, (int)y);
            
            // Label major latitude lines
            if (lat % 40 == 0 && lat != 0) {
                g2d.setColor(Color.DARK_GRAY);
                g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
                String label = (lat > 0) ? lat + "°N" : Math.abs(lat) + "°S";
                g2d.drawString(label, 5, (int)y - 2);
                g2d.setColor(GRID_COLOR);
            }
        }
        
        // Draw equator more prominently
        double equatorMercatorY = Math.log(Math.tan(Math.PI/4));
        double minLatRad = Math.toRadians(-85);
        double maxLatRad = Math.toRadians(85);
        double minMercatorY = Math.log(Math.tan(Math.PI/4 + minLatRad/2));
        double maxMercatorY = Math.log(Math.tan(Math.PI/4 + maxLatRad/2));
        double equatorY = height - ((equatorMercatorY - minMercatorY) / (maxMercatorY - minMercatorY) * height);
        
        g2d.setColor(new Color(255, 0, 0, 180));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(0, (int)equatorY, width, (int)equatorY);
        
        // Draw Prime Meridian
        double primeX = (0 + 180.0) / 360.0 * width;
        g2d.setColor(new Color(0, 0, 255, 180));
        g2d.drawLine((int)primeX, 0, (int)primeX, height);
        
        // Draw map border
        g2d.setColor(Color.DARK_GRAY);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(1, 1, width - 2, height - 2);
        
        // Draw coordinate range labels in corners
        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        g2d.drawString("85°N, 180°W", 5, 15);
        g2d.drawString("85°N, 180°E", width - 80, 15);
        g2d.drawString("85°S, 180°W", 5, height - 5);
        g2d.drawString("85°S, 180°E", width - 80, height - 5);
    }
    
    private void drawPlanes(Graphics2D g2d) {
        List<MapPlane> planes = viewModel.getPlanes();
        
        for (MapPlane plane : planes) {
            drawPlane(g2d, plane);
        }
    }
    
    private void drawPlane(Graphics2D g2d, MapPlane plane) {
        if (plane == null) {
            return;
        }
        
        MapCoordinate pos = plane.getScreenPosition();
        if (pos == null) {
            return;
        }
        
        // Skip if plane is outside visible area
        if (pos.getX() < 0 || pos.getX() > getWidth() || 
            pos.getY() < 0 || pos.getY() > getHeight()) {
            return;
        }
        
        // Save current transform
        AffineTransform oldTransform = g2d.getTransform();
        
        // Translate to plane position and rotate by heading
        g2d.translate(pos.getX(), pos.getY());
        g2d.rotate(Math.toRadians(plane.getHeading()));
        
        // Draw plane symbol (simple triangle/arrow)
        g2d.setColor(PLANE_COLOR);
        g2d.setStroke(new BasicStroke(2));
        
        int[] xPoints = {0, -8, -5, -5, -8, 0, 8, 5, 5, 8};
        int[] yPoints = {-10, 3, 3, 8, 8, 5, 8, 8, 3, 3};
        
        g2d.fillPolygon(xPoints, yPoints, xPoints.length);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(xPoints, yPoints, xPoints.length);
        
        // Restore transform
        g2d.setTransform(oldTransform);
        
        // Draw flight number label
        g2d.setColor(PLANE_LABEL_COLOR);
        g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        FontMetrics fm = g2d.getFontMetrics();
        String label = plane.getFlightNumber();
        int labelWidth = fm.stringWidth(label);
        
        // Position label below and to the right of the plane
        int labelX = pos.getX() + 12;
        int labelY = pos.getY() + 5;
        
        // Draw label background
        g2d.setColor(new Color(255, 255, 255, 180));
        g2d.fillRoundRect(labelX - 2, labelY - fm.getAscent() - 1, 
                         labelWidth + 4, fm.getHeight() + 2, 4, 4);
        
        // Draw label text
        g2d.setColor(PLANE_LABEL_COLOR);
        g2d.drawString(label, labelX, labelY);
        
        // Draw additional info if there's space
        if (getHeight() > 300) {
            String altInfo = String.format("Alt: %,dft", plane.getAltitude());
            g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
            g2d.drawString(altInfo, labelX, labelY + 12);
        }
    }
    
    private void drawStatusMessage(Graphics2D g2d) {
        String message = viewModel.getMessage();
        if (message != null && !message.isEmpty()) {
            g2d.setColor(viewModel.isSuccess() ? new Color(0, 120, 0) : Color.RED);
            g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
            FontMetrics fm = g2d.getFontMetrics();
            
            int x = (getWidth() - fm.stringWidth(message)) / 2;
            int y = getHeight() - 10;
            
            g2d.drawString(message, x, y);
        }
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            // Repaint when view model state changes
            SwingUtilities.invokeLater(this::repaint);
        }
    }
    
    @Override
    public void removeNotify() {
        super.removeNotify();
        // Clean up listener when component is removed
        viewModel.removePropertyChangeListener(this);
    }
}