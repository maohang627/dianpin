package com.thestore.microstore.vo.proxy;

import java.io.Serializable;

public class MyyhdCountyVo
  implements Serializable
{
  private static final long serialVersionUID = -851301862701998371L;
  private Long id;
  private String countyName;
  private String postCode;

  public MyyhdCountyVo()
  {
    this.id = null;

    this.countyName = null;

    this.postCode = null; }

  public Long getId() {
    return this.id; }

  public void setId(Long id) {
    this.id = id; }

  public String getCountyName() {
    return this.countyName; }

  public void setCountyName(String countyName) {
    this.countyName = countyName; }

  public String getPostCode() {
    return this.postCode; }

  public void setPostCode(String postCode) {
    this.postCode = postCode;
  }
}