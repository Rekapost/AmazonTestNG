import http from 'k6/http';   // k6 library , http module to get,put,post..
import {sleep} from 'k6';

export const options = {
//traffic ramp up to 200 users over 1 minute,then it stays there for 5 mins and then ramp down to 0 users 
  stages:[ 
    {duration:'1m', target:50},
    {duration:'5m', target:50},
    {duration:'30s', target:0}
  ]
}

//Load Testing Code
export default function(){
    http.get('https://www.amazon.in/');
    sleep(1); // pause for 1 second between requests

   // k6 run --out json=results.json stress-test.js 
   //jq . results.json
}