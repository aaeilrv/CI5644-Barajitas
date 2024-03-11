/// <reference types="cypress" />

describe("Create New Account", () => {
    beforeEach(() => {
        cy.visit("http://localhost:3000");
        cy.get("#home");
        cy.contains("Regístrate").click();
        
        cy.origin("https://dev-crxb1uekqycez2b5.us.auth0.com/login", () => {
          cy.get("#1-email").type("finamore03@protonmail.com");
          cy.get("#1-password").type("Santoigo16*");
          cy.get("#1-submit").click();
        });
        
        cy.viewport(1280, 720);
        // get to profile
        cy.get('[data-testid="Intercambio"]').click();
      
    });
  
    it("Delete exchange request", () => {
      cy.get('[data-testid="deleteExchangeRequestButton-0"]').should('exist');
      cy.get('[data-testid="deleteExchangeRequestButton-0"]').click();

        // Para obtener el mensaje de alerta
        const stub = cy.stub()  
        cy.on ('window:alert', stub)
        cy
        .get('button').contains('Aceptar').click()
        .then(() => {
        expect(stub.getCall(0)).to.be.calledWith('Request deleted!')      
        })  
    });


  });
  