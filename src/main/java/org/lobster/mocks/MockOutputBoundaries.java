package org.lobster.mocks;

import org.lobster.use_case.add_to_favorites.AddToFavoritesOutputBoundary;
import org.lobster.use_case.add_to_favorites.AddToFavoritesOutputData;
import org.lobster.use_case.remove_from_favorites.RemoveFromFavoritesOutputBoundary;
import org.lobster.use_case.remove_from_favorites.RemoveFromFavoritesOutputData;

/**
 * Mock output boundaries for unit testing
 */
public class MockOutputBoundaries {

    public static class MockAddToFavoritesOutputBoundary implements AddToFavoritesOutputBoundary {
        public AddToFavoritesOutputData lastSuccessOutput = null;
        public String lastFailMessage = null;
        public int successCallCount = 0;
        public int failCallCount = 0;

        public void reset() {
            lastSuccessOutput = null;
            lastFailMessage = null;
            successCallCount = 0;
            failCallCount = 0;
        }

        @Override
        public void prepareSuccessView(AddToFavoritesOutputData outputData) {
            this.lastSuccessOutput = outputData;
            successCallCount++;
        }

        @Override
        public void prepareFailView(String message) {
            this.lastFailMessage = message;
            failCallCount++;
        }
    }

    public static class MockRemoveFromFavoritesOutputBoundary implements RemoveFromFavoritesOutputBoundary {
        public RemoveFromFavoritesOutputData lastSuccessOutput = null;
        public String lastFailMessage = null;
        public int successCallCount = 0;
        public int failCallCount = 0;

        public void reset() {
            lastSuccessOutput = null;
            lastFailMessage = null;
            successCallCount = 0;
            failCallCount = 0;
        }

        @Override
        public void prepareSuccessView(RemoveFromFavoritesOutputData outputData) {
            this.lastSuccessOutput = outputData;
            successCallCount++;
        }

        @Override
        public void prepareFailView(String message) {
            this.lastFailMessage = message;
            failCallCount++;
        }
    }
}