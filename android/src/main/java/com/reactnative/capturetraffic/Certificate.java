package com.reactnative.capturetraffic;

public class Certificate {

    private String alias = "";
    private String password = "";
    private String commonName = "";
    private String organization = "";
    private String organizationalUnitName = "";
    private String certOrganization = "";
    private String certOrganizationalUnitName = "";

    public Certificate(){
    }

    public Certificate(String mAlias, String mPassword, String mCommonName, String mOrganization, String mOrganizationalUnitName,
                       String mCertOrganization, String mCertOrganizationalUnitName){

        this.alias = mAlias;
        this.password = mPassword;
        this.commonName = mCommonName;
        this.organization = mOrganization;
        this.organizationalUnitName = mOrganizationalUnitName;
        this.certOrganization = mCertOrganization;
        this.certOrganizationalUnitName = mCertOrganizationalUnitName;

    }


    public String getAlias() {
        return alias;
    }

    public Certificate setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Certificate setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getCommonName() {
        return commonName;
    }

    public Certificate setCommonName(String commonName) {
        this.commonName = commonName;
        return this;
    }

    public String getOrganization() {
        return organization;
    }

    public Certificate setOrganization(String organization) {
        this.organization = organization;
        return this;
    }

    public String getOrganizationalUnitName() {
        return organizationalUnitName;
    }

    public Certificate setOrganizationalUnitName(String organizationalUnitName) {
        this.organizationalUnitName = organizationalUnitName;
        return this;
    }

    public String getCertOrganization() {
        return certOrganization;
    }

    public Certificate setCertOrganization(String certOrganization) {
        this.certOrganization = certOrganization;
        return this;
    }

    public String getCertOrganizationalUnitName() {
        return certOrganizationalUnitName;
    }

    public Certificate setCertOrganizationalUnitName(String certOrganizationalUnitName) {
        this.certOrganizationalUnitName = certOrganizationalUnitName;
        return this;
    }
}
