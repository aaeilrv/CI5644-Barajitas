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
        cy.get('[data-testid="Perfil"]').click();
      
    });
  
    it("Get to edit Profile", () => {
      cy.get('[data-testid="editProfile"]').click();
  
      cy.url().as("editProfileURL");
      cy.get("@editProfileURL").should("include", "localhost:3000/editProfile");

      cy.get('[data-testid="headerEditProfile"]').should("exist").contains("Editar Perfil");
    });


    it("Clicking the submit button should fail if form isnt valid", () => {
        cy.get('[data-testid="editProfile"]').click();
        cy.get('[data-testid="submitButtonEditProfile"]').click();

        cy.once('fail', (err) => {
            console.log(err.message)
            expect(err.message).to.include('failed because this element is `disabled`')
        })

        cy.get('[data-testid="submitButtonEditProfile"]').click().then(x => {
            // Only here if click succeeds (so test fails)
            done(new Error('Expected button NOT to be clickable, but click() succeeded'));
          })
    })
    it("Updating Profile", () => {
        cy.get('[data-testid="editProfile"]').click();
        
        cy.get('[data-testid="name-input"]').clear().type("jose");
        cy.get('[data-testid="lastName-input"]').clear().type("gonzalez");
        cy.get('[data-testid="username-input"]').clear().type("Boondock132132");
        cy.get('[data-testid="email-input"]').clear().type("jose12356@gmail.com");
    
        cy.get('[data-testid="submitButtonEditProfile"]').click();

    })

  });
  