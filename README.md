# WebSocketDemo

I) Use the Websocket APIs documented here - https://www.blockchain.com/en/api/api_websocket
Make the following subscriptions when the app is opened:
1. Subscribing to new Blocks
2. Subscribing to new Unconfirmed transactions
Display the status of websocket connection somewhere on the screen. - ‘Connected, Connecting..,
Disconnected’

II) Display the following information when a new block arrives for the first time. Keep updating the
information on every new block arrival.
- Block hash
- Block Height
- Total BTC sent
- Reward

Libraries used : 
- OkHttp

# To Do
Maintain a queue containing the 5 latest unconfirmed transactions with ‘value’ greater than $100. Keep
updating the queue as the new unconfirmed transactions keep coming. For all the transactions in the
queue, display the following information -
- Transaction hash
- Transaction amount in USD. Use any online public REST API for BTC/USD price
- Time of the transaction in IST (dd-mm-yyyy hh:mm:ss +05:30)
