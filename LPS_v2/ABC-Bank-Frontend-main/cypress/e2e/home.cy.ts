describe('Check Command Delay', ()=> {
  it('should apply a 1-second delay to commands', ()=> {
    cy.visit('http://localhost:4200/');
    
    // Add additional commands to check the delay effect
    cy.get('#submitloan').click();
    

    cy.get('#firstName').type('Neeru');
    cy.get('#lastName').type('Giri').wait(1000);

    
    cy.get('#dateOfBirth').type('1990-01-01');
    
    cy.get('#maritalStatus').select('Single');
    cy.get('#ssnNumber').type('1024').wait(1000);


    cy.get('#loanAmount').type('10000');
    cy.get('#loanPurpose').select('Debt');
    cy.get('#addressLine1').type('123 Main St');
    cy.get('#addressLine2').type('Apt 4B');
    cy.get('#city').type('Anytown');
    cy.get('#state').type('NY');
    cy.get('#postalCode').type('12345');
    cy.get('#phoneHome').type('7845678980');
    cy.get('#phoneMobile').type('7845678980');
    cy.get('#emailAddress').type('qfGKl@example.com').wait(1000);

    cy.get('#employerName').type('Shubh');
    cy.get('#annualSalary').type('10000000');

    cy.get('#workExperienceYears').type('5');
    cy.get('#workExperienceMonths').type('6').wait(1000);

    cy.get('#employerAddressLine1').type('D 227');
    cy.get('#employerCity').type('Bangalore');
    cy.get('#employerState').type('Karnataka');
    cy.get('#employerPostalCode').type('56005').wait(1000);

    cy.get('#submit').click()

    cy.get('#viewApplications').click().wait(10000)

    cy.get('table tr:last-child td a').click();

  });
});
