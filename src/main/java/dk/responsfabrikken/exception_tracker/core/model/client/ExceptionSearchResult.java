package dk.responsfabrikken.exception_tracker.core.model.client;

import java.util.List;

public class ExceptionSearchResult {
    private int currentPage;
    private long totalItems;
    private List<ExceptionGroupDto> data;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public List<ExceptionGroupDto> getData() {
        return data;
    }

    public void setData(List<ExceptionGroupDto> data) {
        this.data = data;
    }
}
