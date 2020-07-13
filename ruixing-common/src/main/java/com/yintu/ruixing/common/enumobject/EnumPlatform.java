package com.yintu.ruixing.common.enumobject;

/**
 * @author:mlf
 * @date:2020/7/13 14:20
 */
public enum EnumPlatform {
    Any("any"),
    Linux("Linux"),
    Mac_OS("Mac OS"),
    Mac_OS_X("Mac OS X"),
    Windows("Windows"),
    OS2("OS/2"),
    Solaris("Solaris"),
    SunOS("SunOS"),
    MPEiX("MPE/iX"),
    HP_UX("HP-UX"),
    AIX("AIX"),
    OS390("OS/390"),
    FreeBSD("FreeBSD"),
    Irix("Irix"),
    Digital_Unix("Digital Unix"),
    NetWare_411("NetWare"),
    OSF1("OSF1"),
    OpenVMS("OpenVMS"),
    Others("Others");


    private final String description;

    EnumPlatform(String desc) {
        this.description = desc;
    }

    public String toString() {
        return description;
    }

}
