package com.bappi.videoinventorymanagement.utils;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.List;

@JsonDeserialize(
        builder = ResponsePayload.ResponsePayloadBuilder.class
)
public class ResponsePayload<T> {
    final Integer totalPages;
    final Integer currentPage;
    final List<T> dataList;

    public static <T> ResponsePayloadBuilder<T> builder() {
        return new ResponsePayloadBuilder<>();
    }

    public Integer getTotalPages() {
        return this.totalPages;
    }

    public Integer getCurrentPage() {
        return this.currentPage;
    }

    public List<T> getDataList() {
        return this.dataList;
    }

    public ResponsePayload(final Integer totalPages, final Integer currentPage, final List<T> dataList) {
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.dataList = dataList;
    }

    @JsonPOJOBuilder(
            withPrefix = "",
            buildMethodName = "build"
    )
    public static class ResponsePayloadBuilder<T> {
        private Integer totalPages;
        private Integer currentPage;
        private List<T> dataList;

        ResponsePayloadBuilder() {
        }

        public ResponsePayloadBuilder<T> totalPages(final Integer totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public ResponsePayloadBuilder<T> currentPage(final Integer currentPage) {
            this.currentPage = currentPage;
            return this;
        }

        public ResponsePayloadBuilder<T> dataList(final List<T> dataList) {
            this.dataList = dataList;
            return this;
        }

        public ResponsePayload<T> build() {
            return new ResponsePayload(this.totalPages, this.currentPage, this.dataList);
        }

        public String toString() {
            return "ResponsePayload.ResponsePayloadBuilder(totalPages=" + this.totalPages + ", currentPage=" + this.currentPage + ", dataList=" + this.dataList + ")";
        }
    }
}
