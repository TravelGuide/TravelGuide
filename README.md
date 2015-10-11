# Group Project - *Travel Guide*

**Travel Guide** is an android app that allows a user plan a trip based on preferences and crowd sourced information. The app also utilizes the [TripAdvisor APIs](https://developer-tripadvisor.com/content-api/).

Time spent: **XX** hours spent in total

## User Stories

The following **required** functionality is completed:

* [ ]	Users can sign up for a new **Travel Guide** user account
* [ ]	Users can log in to their **Travel Guide** user account
* [ ] Users are able to view their profile - Name, profile picture, recent trips
* [ ] Users can upload their trip experiences - Location, days travelled, number of people, pictures, hotel, cost etc.
* [ ] Users can view others' trip experiences
* [ ] Authentication is done using **OAuth**
* [ ] Data storage is done using **Parse**
* [ ] Data is stored for offline access in **SQLite** database and disk
* [ ] Data is retreived from local storage using ORM **Active Android**
* [ ] Data is also obtained using **[TripAdvisor APIs](https://developer-tripadvisor.com/content-api/)**
    
The following **optional** features are implemented:
* [ ] Users can add other users' trips to their **favorites**
* [ ] Users can **rate** other users' trips (upto 5 stars)
* [ ] Users can post **reviews** on other users' trips
* [ ] **Location based** information is pushed to users' phone

The following **bonus** features are implemented:

* [ ] Users can **follow** other users and get trip details on their activity feed
* [ ] Users can request a **trip plan** and the app will provide one based on available data
* [ ] **Trip recommendations** are provided based on users' preferences/past trips/favorites

The following **additional** features are implemented:

* [ ] Location based **real time data** (e.g. some event happening locally) is pushed to the users' phone

## Wireframes

![Wireframe](Wireframes/Login_1.png)
![Wireframe](Wireframes/Login_2.png)
![Wireframe](Wireframes/SignUp.png)
![Wireframe](Wireframes/Profile.png)

## Video Walkthrough

Here's a walkthrough of implemented user stories:

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android

## License

    Copyright [2015] kprav, SharedMocha, rafagcamargo

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.