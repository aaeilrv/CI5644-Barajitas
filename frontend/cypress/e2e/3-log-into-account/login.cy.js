/// <reference types="cypress" />

describe('Login with Credentials', () => {
    before(() => {
        cy.visit('http://localhost:3000');
    })

    it('Acess Auth0 login page', () => {
        let auth0URL;
        let albumURL
        cy.get('#home')
        cy.contains('Regístrate').click()

        cy.wait(1000)
        cy.origin('https://dev-crxb1uekqycez2b5.us.auth0.com/login', () => {
            cy.get('#1-email').type('finamore03@protonmail.com')
            cy.get('#1-password').type('Santoigo16*')
            cy.get('#1-submit').click()
        } )

        cy.wait(2000)
        cy.url().as('albumURL')
        cy.get('@albumURL').should('include', 'http://localhost:3000/album')
    })
})