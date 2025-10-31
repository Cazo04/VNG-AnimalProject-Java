
package com.example.staff.event;

public class StaffUpdatedEvent {
    private String requestId;
    private Long staffId;
    private String code;
    private String fullName;
    private String email;
    private String role;
    private String status;

    public StaffUpdatedEvent(String requestId, Long staffId, String code, String fullName, String email, String role, String status) {
        this.requestId = requestId;
        this.staffId = staffId;
        this.code = code;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.status = status;
    }

    public String getRequestId() { return requestId; }
    public Long getStaffId() { return staffId; }
    public String getCode() { return code; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getStatus() { return status; }
}
