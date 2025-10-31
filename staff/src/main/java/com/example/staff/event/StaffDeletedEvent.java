package com.example.staff.event;

public class StaffDeletedEvent {
    private Long staffId;

    public StaffDeletedEvent(Long staffId) {
        this.staffId = staffId;
    }

    public Long getStaffId() { return staffId; }
}
