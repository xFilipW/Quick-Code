package com.example.quickcode.loginRegister;

import com.example.quickcode.common.utils.StringUtils;

import org.junit.Assert;
import org.junit.Test;

public class SignUpTabFragmentTest {

    @Test
    public void shouldReturnCorrectPhonePattern() {
        Assert.assertEquals("123 456 78", StringUtils.splitToGroupsAndJoin(3, "12345678", " "));
        Assert.assertEquals("123 456 7", StringUtils.splitToGroupsAndJoin(3, "1234567", " "));
        Assert.assertEquals("123 456", StringUtils.splitToGroupsAndJoin(3, "123456", " "));
        Assert.assertEquals("123 45", StringUtils.splitToGroupsAndJoin(3, "12345", " "));
        Assert.assertEquals("123 4", StringUtils.splitToGroupsAndJoin(3, "1234", " "));
        Assert.assertEquals("123", StringUtils.splitToGroupsAndJoin(3, "123", " "));
        Assert.assertEquals("12", StringUtils.splitToGroupsAndJoin(3, "12", " "));
        Assert.assertEquals("1", StringUtils.splitToGroupsAndJoin(3, "1", " "));
        Assert.assertEquals("", StringUtils.splitToGroupsAndJoin(3, "", " "));
    }
}