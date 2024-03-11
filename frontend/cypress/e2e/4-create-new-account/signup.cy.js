/// <reference types="cypress" />

describe('Create New Account', () => {
    before(() => {
        cy.visit('http://localhost:3000')
    })

    it('Auth0 Sign Up', () => {
        cy.get('#home').contains('Regístrate').click()

        cy.origin('https://dev-crxb1uekqycez2b5.us.auth0.com/login', () => {
            cy.get('.auth0-lock-tabs li').contains('Sign Up').click()
            cy.get('#1-email').type('abcd1234@cantv.net')
            cy.get('#1-password').type('Barajita1*')
            cy.get('#1-submit').click()
            cy.get('#allow').click()
        })

        cy.url().as('signUpURL')
        cy.get('@signUpURL').should('include', 'localhost:3000/register')
        cy.get('#username-input').type('usuarioPrueba1')
        cy.get('#firstName-input').type('Pedro')
        cy.get('#lastName-input').type('Pérez')
        cy.get('#email-input').type('abcd1234@cantv.net')
        cy.get('#birthday-input').type('1995-02-27')

        cy.contains('Regístrate').click()

        cy.url().as('albumURL')
        cy.get('@albumURL').should('include', 'localhost:3000/album')
    })

})