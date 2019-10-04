package ir.shirazservice.expert.webservice.getservicemaninfo;

public class ServiceMan {

    private String firstName;

    private String lastName;

    private int servicemanId;

    private String picAddress;

    private int personTypeIdx;

    private String personType;

    private int titleId;

    private String title;

    private int provinceId;

    private String province;

    private int cityId;

    private String city;

    private int areaId;

    private String area;

    private String address;

    private int totalPoint;

    private double rating;

    private int commentsCount;

    private int medalsCount;

    private String credit;

    private String tempCredit;

    private int discountPercent;

    private String records;

    private String documents;

    private int lastLoginDate;

    private String lastLoginDatePersian;

    private String lastLoginIp;

    private int membershipTime;

    private String birthdayTimePersian;

    private int birthdayTime;

    private int age;

    private int finishedServicesCount;

    private String insrtTimePersian;

    private int insrtTime;

    
    private String accessToken;

    public ServiceMan() {
    }

    public ServiceMan(String firstName, String lastName, int servicemanId, String picAddress, int personTypeIdx, String personType, int titleId, String title, int provinceId, String province, int cityId, String city, int areaId, String area, String address, int totalPoint, double rating, int commentsCount, int medalsCount, String credit, String tempCredit, int discountPercent, String records, String documents, int lastLoginDate, String lastLoginDatePersian, String lastLoginIp, int membershipTime, String birthdayTimePersian, int birthdayTime, int age, int finishedServicesCount, String insrtTimePersian, int insrtTime, String accessToken) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.servicemanId = servicemanId;
        this.picAddress = picAddress;
        this.personTypeIdx = personTypeIdx;
        this.personType = personType;
        this.titleId = titleId;
        this.title = title;
        this.provinceId = provinceId;
        this.province = province;
        this.cityId = cityId;
        this.city = city;
        this.areaId = areaId;
        this.area = area;
        this.address = address;
        this.totalPoint = totalPoint;
        this.rating = rating;
        this.commentsCount = commentsCount;
        this.medalsCount = medalsCount;
        this.credit = credit;
        this.tempCredit = tempCredit;
        this.discountPercent = discountPercent;
        this.records = records;
        this.documents = documents;
        this.lastLoginDate = lastLoginDate;
        this.lastLoginDatePersian = lastLoginDatePersian;
        this.lastLoginIp = lastLoginIp;
        this.membershipTime = membershipTime;
        this.birthdayTimePersian = birthdayTimePersian;
        this.birthdayTime = birthdayTime;
        this.age = age;
        this.finishedServicesCount = finishedServicesCount;
        this.insrtTimePersian = insrtTimePersian;
        this.insrtTime = insrtTime;
        this.accessToken = accessToken;
    }

    private String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    private String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getServicemanId() {
        return servicemanId;
    }

    public void setServicemanId(int servicemanId) {
        this.servicemanId = servicemanId;
    }

    public String getPicAddress() {
        return picAddress;
    }

    public void setPicAddress(String picAddress) {
        this.picAddress = picAddress;
    }

    public int getPersonTypeIdx() {
        return personTypeIdx;
    }

    public void setPersonTypeIdx(int personTypeIdx) {
        this.personTypeIdx = personTypeIdx;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    private String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public int getMedalsCount() {
        return medalsCount;
    }

    public void setMedalsCount(int medalsCount) {
        this.medalsCount = medalsCount;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getTempCredit() {
        return tempCredit;
    }

    public void setTempCredit(String tempCredit) {
        this.tempCredit = tempCredit;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getRecords() {
        return records;
    }

    public void setRecords(String records) {
        this.records = records;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public int getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(int lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getLastLoginDatePersian() {
        return lastLoginDatePersian;
    }

    public void setLastLoginDatePersian(String lastLoginDatePersian) {
        this.lastLoginDatePersian = lastLoginDatePersian;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public int getMembershipTime() {
        return membershipTime;
    }

    public void setMembershipTime(int membershipTime) {
        this.membershipTime = membershipTime;
    }

    public String getBirthdayTimePersian() {
        return birthdayTimePersian;
    }

    public void setBirthdayTimePersian(String birthdayTimePersian) {
        this.birthdayTimePersian = birthdayTimePersian;
    }

    public int getBirthdayTime() {
        return birthdayTime;
    }

    public void setBirthdayTime(int birthdayTime) {
        this.birthdayTime = birthdayTime;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getFinishedServicesCount() {
        return finishedServicesCount;
    }

    public void setFinishedServicesCount(int finishedServicesCount) {
        this.finishedServicesCount = finishedServicesCount;
    }

    public String getInsrtTimePersian() {
        return insrtTimePersian;
    }

    public void setInsrtTimePersian(String insrtTimePersian) {
        this.insrtTimePersian = insrtTimePersian;
    }

    public int getInsrtTime() {
        return insrtTime;
    }

    public void setInsrtTime(int insrtTime) {
        this.insrtTime = insrtTime;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getFullName()
    {
        return String.format("%s %s %s", getTitle(),getFirstName(), getLastName());

    }
}
