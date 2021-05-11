addEventListener("fetch", event => {
  console.log(`Received new request: ${event.request.url}`)

  event.respondWith(handleRequest(event.request))
})

async function handleRequest(request) {
  const username = 'ACa2a8e36fd8f64f403c8a4026adce8845';
  const password = pnva_twilio_password;
  const basicAuth =
      'Basic ' + btoa(`${username}:${password}`);

  let phoneNumber = (await request.json()).phoneNumber;

  return await fetch(
      new Request(`https://lookups.twilio.com/v1/PhoneNumbers/${phoneNumber}`, {
        headers: {
          'Authorization': basicAuth
        },
        method: 'GET'
      }))
}
