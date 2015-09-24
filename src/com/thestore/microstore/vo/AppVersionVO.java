package com.thestore.microstore.vo;

import com.thestore.microstore.vo.commom.BaseEntity;

public class AppVersionVO extends BaseEntity {

	private static final long serialVersionUID = -5115693618346442354L;
	/**
	 * 是否更新
	 */
	private boolean canUpgrade;
	/**
	 * 是否强制更新
	 */
	private boolean forceUpgrade;
	/**
	 * 当前版本
	 */
	private String currentVersion;
    /**
     * 更新简介
     */
	private String upgradeKeyword;
	/**
	 * 更新说明
	 */
	private String upgradeDesc;
	/**
	 * apk下载地址
	 */
	private String apkDownLoadUrl;
	
	
	public boolean isCanUpgrade() {
		return canUpgrade;
	}
	public void setCanUpgrade(boolean canUpgrade) {
		this.canUpgrade = canUpgrade;
	}
	public boolean isForceUpgrade() {
		return forceUpgrade;
	}
	public void setForceUpgrade(boolean forceUpgrade) {
		this.forceUpgrade = forceUpgrade;
	}
	public String getCurrentVersion() {
		return currentVersion;
	}
	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}
	public String getUpgradeKeyword() {
		return upgradeKeyword;
	}
	public void setUpgradeKeyword(String upgradeKeyword) {
		this.upgradeKeyword = upgradeKeyword;
	}
	public String getUpgradeDesc() {
		return upgradeDesc;
	}
	public void setUpgradeDesc(String upgradeDesc) {
		this.upgradeDesc = upgradeDesc;
	}
	public String getApkDownLoadUrl() {
		return apkDownLoadUrl;
	}
	public void setApkDownLoadUrl(String apkDownLoadUrl) {
		this.apkDownLoadUrl = apkDownLoadUrl;
	}	
    
}
