package com.gestionrh

import grails.gorm.transactions.Transactional
import org.springframework.security.crypto.password.PasswordEncoder

class BootStrap {

    PasswordEncoder passwordEncoder

    def init = {
        addRolesAndUsers()
    }

    @Transactional
    void addRolesAndUsers() {

        def rhRole = new Role(
                authority: 'ROLE_RH'
        ).save()

        def userRole = new Role(
                authority: 'ROLE_USER'
        ).save()

        def User1 = new User(
                email: 'user1@gmail.com',
                password: passwordEncoder.encode('mdp1')
        ).save()

        def User2 = new User(
                email: 'user2@gmail.com',
                password: passwordEncoder.encode('mdp2')
        ).save()

        def User3 = new User(
                email: 'user3@gmail.com',
                password: passwordEncoder.encode('mdp3')
        ).save()

        // User-role
        UserRole.create User1, rhRole
        UserRole.create User2, userRole
        UserRole.create User3, userRole

        // Creation in db and clear hibernate cache
        UserRole.withSession {
            it.flush()
            it.clear()
        }

    }

    def destroy = {
    }

}