# api and expected request/response

POST `http://localhost:8080/application`
```
{
    "name": {
        "firstName": "Mary",
        "middleName": "",
        "lastName": "A."
    },
    "dateOfBirth": "1985-08-15",
    "maritalStatus": "Married",
    "address": {
        "addressLine1": "123 Main St",
        "addressLine2": "Apt 4B",
        "city": "Springfield",
        "state": "IL",
        "postalCode": "62704"
    },
    "ssnNumber": 54734,
    "phoneNumber": {
        "homePhone": "2175551234",
        "officePhone": "2175555678",
        "mobilePhone": "2175559876"
    },
    "emailAddress": "john.doe@example.com",
    "loanAmount": 15000.00,
    "loanPurpose": "Home_Loan",
    "description": "Loan for home renovation",
    "employmentDetails": {
        "workExperience": 10,
        "annualSalary": 85000.00,
        "employerName": "ABC Corp",
        "currentEmployerAddress": {
            "addressLine1": "456 Elm St",
            "addressLine2": "Suite 500",
            "city": "Springfield",
            "state": "IL",
            "postalCode": "62701"
        },
        "designation": "Software Engineer"
    }
}
```

GET `http://localhost:8080/application`
```
{
    "id": 3,   //this is loan ID
    "application": {
        "id": 3,    //this is application ID
        "name": {
            "firstName": "Mary",
            "middleName": "",
            "lastName": "A."
        },
        "dateOfBirth": "1985-08-15",
        "maritalStatus": "Married",
        "address": {
            "addressLine1": "123 Main St",
            "addressLine2": "Apt 4B",
            "city": "Springfield",
            "state": "IL",
            "postalCode": "62704"
        },
        "ssnNumber": 54734,
        "phoneNumber": {
            "homePhone": "2175551234",
            "officePhone": "2175555678",
            "mobilePhone": "2175559876"
        },
        "emailAddress": "john.doe@example.com",
        "loanAmount": 15000.0,
        "loanPurpose": "Home_Loan",
        "description": "Loan for home renovation",
        "employmentDetails": {
            "workExperience": 10,
            "annualSalary": 85000.0,
            "employerName": "ABC Corp",
            "currentEmployerAddress": {
                "addressLine1": "456 Elm St",
                "addressLine2": "Suite 500",
                "city": "Springfield",
                "state": "IL",
                "postalCode": "62701"
            },
            "designation": "Software Engineer"
        }
    },
    "applicationStatus": "In_Progress",
    "score": 0,
    "reason": null,
    "submittedDate": "2024-07-29"
}
```

GET `http://localhost:8080/applications`
```
[
    {
        "applicationID": 1,
        "name": {
            "firstName": "Lia",
            "middleName": "",
            "lastName": "A."
        },
        "submittedDate": "2024-07-29",
        "applicationStatus": "In_Progress"
    },
    {
        "applicationID": 2,
        "name": {
            "firstName": "John",
            "middleName": "",
            "lastName": "A."
        },
        "submittedDate": "2024-07-29",
        "applicationStatus": "In_Progress"
    },
    {
        "applicationID": 3,
        "name": {
            "firstName": "Mary",
            "middleName": "",
            "lastName": "A."
        },
        "submittedDate": "2024-07-29",
        "applicationStatus": "In_Progress"
    }
]
```
