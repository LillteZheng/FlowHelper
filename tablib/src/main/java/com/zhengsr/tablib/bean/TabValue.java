package com.zhengsr.tablib.bean;

public class TabValue {
        public float left;
        public float right;

        public TabValue(float left, float right) {
            this.left = left;
            this.right = right;
        }

        public TabValue() {
        }

        @Override
        public String toString() {
            return "TabValue{" +
                    "left=" + left +
                    ", right=" + right +
                    '}';
        }
    }