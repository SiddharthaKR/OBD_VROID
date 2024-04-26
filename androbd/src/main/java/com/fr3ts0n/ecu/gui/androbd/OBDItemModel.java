package com.fr3ts0n.ecu.gui.androbd;

public class OBDItemModel {
        // Variables for OBD item properties
        private String timestamp;
        private String pid;
        private String value;
        private String units;
        private int id;

        // Getter and setter methods
        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getUnits() {
            return units;
        }

        public void setUnits(String units) {
            this.units = units;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        // Constructor
        public OBDItemModel(String timestamp, String pid, String value, String units) {
            this.timestamp = timestamp;
            this.pid = pid;
            this.value = value;
            this.units = units;
        }
    }

