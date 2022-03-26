package com.test.flipgrid.models

import com.test.flipgrid.models.entity.UserProfileEntity
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class UserProfileEntityTest {
    lateinit var userProfileEntity: UserProfileEntity
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        buildUserProfileEntity()
    }

    @Test
    fun UserProfileEntity_NotNull_Test() {
        TestCase.assertNotNull(userProfileEntity)
    }

    @Test
    fun UserProfileEntity_Fname_Test() {
        TestCase.assertEquals(userProfileEntity.fName, "ravi")
    }

    @Test
    fun UserProfileEntity_Lname_Test() {
        TestCase.assertEquals(userProfileEntity.lName, "kkk")
    }

    @Test
    fun UserProfileEntity_Email_Test() {
        TestCase.assertEquals(userProfileEntity.email, "ravik@gmail.com")
    }


    @Test
    fun UserProfileEntity_Edu_Test() {
        TestCase.assertEquals(userProfileEntity.password, "test1234")
    }

    @Test
    fun UserProfileEntity_Exp_Test() {
        TestCase.assertEquals(userProfileEntity.website, "www.google.com")
    }

    private fun buildUserProfileEntity() {
        userProfileEntity = UserProfileEntity(1, "ravi", "kkk", "ravik@gmail.com", "test1234", "www.google.com",  null)
    }
}