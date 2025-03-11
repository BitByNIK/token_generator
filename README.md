# Producer App

## Overview
The Producer App is responsible for generating tokens that include a timestamp and geolocation coordinates. It stores these tokens in a database and exposes them to trusted Consumer apps via a Content Provider.

## Components
- **tokenGenerationUserControl Activity:**  
  Provides two buttons:
    - **Start Token Generation:** Starts the `generateTokens` service.
    - **Stop Token Generation:** Stops the service.  
      This activity also displays the generated tokens.

- **generateTokens Service:**  
  Generates one token every 15 seconds. Each token consists of:
    - **Timestamp:** Formatted as `04-02-2025T05:00:00`
    - **Geo-coordinates:** Latitude and Longitude (e.g., `28.543680, 77.198692`)

- **TokenRepository:**  
  A database table that stores all generated tokens.

- **provideTokens Content Provider:**  
  Exposes read-only access to the tokens stored in the database for trusted Consumer apps.

## How It Works
1. The user initiates token generation from the `tokenGenerationUserControl` activity.
2. The `generateTokens` service begins creating tokens at 15‑second intervals.
3. Tokens are stored in the `TokenRepository` database.
4. The `provideTokens` Content Provider makes token data available to the Consumer App.
5. After every 4 tokens, a broadcast is sent to notify the Consumer App.
