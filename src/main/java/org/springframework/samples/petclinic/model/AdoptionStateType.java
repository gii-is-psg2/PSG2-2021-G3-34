package org.springframework.samples.petclinic.model;

public enum AdoptionStateType {
    PENDING("Pending"),
    ACCEPTED("Accepted"),
    DECLINED("Declined");

    private String name;

    AdoptionStateType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}