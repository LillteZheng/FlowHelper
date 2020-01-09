package com.zhengsr.tablib.bean;

public class TabTypeValue {
        public float left;
        public float right;

        public TabTypeValue(float left, float right) {
            this.left = left;
            this.right = right;
        }

        public TabTypeValue() {
        }

        @Override
        public String toString() {
            return "TabTypeValue{" +
                    "left=" + left +
                    ", right=" + right +
                    '}';
        }
    }