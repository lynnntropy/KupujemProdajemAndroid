package com.omegavesko.kupujemprodajem;

public class UserPersonalInfo
{
    public String userFullName;
    public String userEmail;
    public String userPositiveVotes;
    public String userNegativeVotes;

    public String userPhoneNumber;
    public String userLocation;

    public UserPersonalInfo() {} // empty constructor for convenience

    public UserPersonalInfo(String userFullName, String userEmail, String userPositiveVotes, String userNegativeVotes, String userPhoneNumber, String userLocation)
    {
        this.userFullName = userFullName;
        this.userEmail = userEmail;
        this.userPositiveVotes = userPositiveVotes;
        this.userNegativeVotes = userNegativeVotes;
        this.userPhoneNumber = userPhoneNumber;
        this.userLocation = userLocation;
    }

    @Override
    public String toString()
    {
        return "UserPersonalInfo{" +
                "userFullName='" + userFullName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPositiveVotes='" + userPositiveVotes + '\'' +
                ", userNegativeVotes='" + userNegativeVotes + '\'' +
                ", userPhoneNumber='" + userPhoneNumber + '\'' +
                ", userLocation='" + userLocation + '\'' +
                '}';
    }
}
