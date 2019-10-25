package ir.shirazservice.expert.webservice.getservicemaninfobyid;

public class ServiceManInfoDetail {


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
    private Float rating;
    private int commentsCount;
    private int medalsCount;
    private String credit;
    private String tempCredit;
    private int discountPercent;
    private String records;
    private String documents;
    private int lastLoginDate;
    private String lastLoginDatePersian;
    private String lastLoginIP;
    private int membershipTime;
    private String birthdayTimePersian;
    private int birthdayTime;
    private int age;
    private int finishedServicesCount;
    private String insrtTimePersian;
    private int insrtTime;

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

    public String getTitle() {
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

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
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

    public String getLastLoginIP() {
        return lastLoginIP;
    }

    public void setLastLoginIP(String lastLoginIP) {
        this.lastLoginIP = lastLoginIP;
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

    public String getFullName() {
        return String.format("%s %s", getFirstName(), getLastName());
    }

}
