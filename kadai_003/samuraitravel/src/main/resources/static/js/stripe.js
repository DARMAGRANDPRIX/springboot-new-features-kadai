 const stripe = Stripe('pk_test_51PCNMkGcxn0dKbUl25s8OpDuXsy7ms8xdSM7bGuCevwapmo1KaOzRNDitRaEKIkbRkL46eyYOInpHGiRq84KMnF200JwSnWdLO');
 const paymentButton = document.querySelector('#paymentButton');
 
 paymentButton.addEventListener('click', () => {
   stripe.redirectToCheckout({
     sessionId: sessionId
   })
 });