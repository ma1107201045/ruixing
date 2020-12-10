package com.yintu.ruixing.yuanchengzhichi;

public class AlarmTicketEntityWithBLOBs extends AlarmTicketEntity {
    private String reasonAnalysis;

    private String treatmentMeasure;

    private String dataFeature;

    private String faultEquipment;

    public String getReasonAnalysis() {
        return reasonAnalysis;
    }

    public void setReasonAnalysis(String reasonAnalysis) {
        this.reasonAnalysis = reasonAnalysis == null ? null : reasonAnalysis.trim();
    }

    public String getTreatmentMeasure() {
        return treatmentMeasure;
    }

    public void setTreatmentMeasure(String treatmentMeasure) {
        this.treatmentMeasure = treatmentMeasure == null ? null : treatmentMeasure.trim();
    }

    public String getDataFeature() {
        return dataFeature;
    }

    public void setDataFeature(String dataFeature) {
        this.dataFeature = dataFeature == null ? null : dataFeature.trim();
    }

    public String getFaultEquipment() {
        return faultEquipment;
    }

    public void setFaultEquipment(String faultEquipment) {
        this.faultEquipment = faultEquipment == null ? null : faultEquipment.trim();
    }
}