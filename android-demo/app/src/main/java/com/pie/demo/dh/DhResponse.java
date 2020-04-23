package com.pie.demo.dh;

import java.util.Objects;

public class DhResponse {
    private int code;
    private boolean success;
    private Message message;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DhResponse that = (DhResponse) o;
        return code == that.code &&
                success == that.success &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, success, message);
    }

    @Override
    public String toString() {
        return "DhResponse{" +
                "code=" + code +
                ", success=" + success +
                ", message=" + message +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public static class Message {
        private String globa;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Message message = (Message) o;
            return Objects.equals(globa, message.globa);
        }

        @Override
        public int hashCode() {
            return Objects.hash(globa);
        }

        @Override
        public String toString() {
            return "Message{" +
                    "globa='" + globa + '\'' +
                    '}';
        }

        public String getGloba() {
            return globa;
        }

        public void setGloba(String globa) {
            this.globa = globa;
        }
    }
}
