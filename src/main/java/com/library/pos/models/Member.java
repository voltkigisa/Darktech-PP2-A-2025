package com.library.pos.models;

public class Member {
    private int id;
    private String member_code;
    private String name;
    private String age;
    private String phone;
    private String address;

    public Member() {}

    public Member(int id, String member_code, String name, String age, String phone, String address) {
        this.id = id;
        this.member_code = member_code;
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.address = address;
    }

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMember_code() { return member_code; }
    public void setMember_code(String member_code) { this.member_code = member_code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}