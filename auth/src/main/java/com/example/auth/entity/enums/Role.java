package com.example.auth.entity.enums;

public enum Role {
        ADMIN("admin"),
        OPERATOR("operator");

        private final String value;

        Role(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
