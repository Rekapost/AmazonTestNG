import http from 'k6/http';
import { sleep, check } from 'k6';

export const options = {
    stages: [
        { duration: '3m', target: 10 },
        { duration: '3m', target: 10 },
        { duration: '1m', target: 10 }
      ]
};

export default function () {
  // Simulate GET request to home page
  let homeRes = http.get('https://www.amazon.in/');
  check(homeRes, {
    'Home page loaded': (r) => r.status === 200,
  });

  // Print the response object as JSON (formatted)
  console.log(JSON.stringify(homeRes, null, 2)); // "null, 2" adds indentation for readability

/*
  // Simulate POST request for login (if applicable)
  let loginPayload = { username: 'testuser', password: 'password123' };
  let loginPostRes = http.post('https://naveenautomationlabs.com/opencart/index.php?route=account/login', loginPayload);
  check(loginPostRes, {
    'Login successful': (r) => r.status === 200,
  });

  // Print the response object as JSON (formatted)
  console.log(JSON.stringify(loginPostRes, null, 2)); // "null, 2" adds indentation for readability
*/
  sleep(1); // pause for 1 second
}

//k6 run --out json=results.json load-test.js
//jq . results.json