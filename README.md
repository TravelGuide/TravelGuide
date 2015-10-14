# Project  - *Travel Guide*

**Travel Guide** is an android app that allows a user to check out popular travel plans created by other travelers. It allows a user plan a trip based on preferences and crowd sourced information. The app utilizes information from travelers, location services, photos and [Trip Advisor APIs](https://developer-tripadvisor.com/content-api/)* to display information and images about tourist places across the globe.

\* Still pending of authorization from TripAdvisor they have a police that requires it.

Today, travelers spend up to three hours crawling the web (Ex: [TripAdvisor](www.tripadvisor.com)) in finding the places to visit/stay when planning for a trip. They do it because they want to tailor a plan to suit their needs/family needs so everyone can have a good time.

What we can do better is create an app where users can create travel/trip plans from their recent trips which can be shared with other travelers. By doing this travelers can spend less time in tailoring a plan and can spend more time on needed things. We will make an Android app as simple as possible by displaying places, others users plan.

In the future we could integrate with [TripAdvisor](www.tripadvisor.com) or [Yelp](www.yelp.com) to pull data. By now we're going to use Android features such as: camera, persitence and location services.

Time spent: **X** hours spent in total

## User Stories

**ArchTypes - Login, Register and Profile - PRAVEEN**

The following **required** functionality is completed:

* [ ]	Users can sign up for a new **Travel Guide** user account (OK)
* [ ]	Users can log in to their **Travel Guide** user account (OK)
* [ ] Users are able to view their profile - Name, profile picture, recent trips (plans of trip) (OK)
* [ ] Users can upload their trip experiences - Location (still pending to check available APIS), days travelled, number of people, pictures, hotel, cost. (OK)
* [ ] Users can view others' trip experiences (OK)
* [ ] Authentication is done using **Parse** - Integrate with **Facebook/Twitter**. (OK)
* [ ] Data storage is done using **Parse** (OK)
* [ ] Data is stored for offline access in **SQLite** database and disk (checking if Parse takes care of that) (OK)
* [ ] Data is retreived from local storage using ORM **Active Android** (OK)
    
The following **optional** features are implemented:
* [ ] Users can add other users' trips to their **favorites**
* [ ] Users can **rate** other users' trips (upto 5 stars)
* [ ] Users can post **reviews** on other users' trips
* [ ] **Location based** information is pushed to users' phone

The following **bonus** features are implemented:

* [ ] Users can **follow** other users and get trip details on their activity feed
* [ ] Users can request a **trip plan** and the app will provide one based on available data
* [ ] **Trip recommendations** are provided based on users' preferences/past trips/favorites
* [ ] Data is also obtained using **[TripAdvisor APIs](https://developer-tripadvisor.com/content-api/)**

The following **additional** features are implemented:
* [ ] Location based **real time data** (e.g. some event happening locally) is pushed to the users' phone

ArchTypes - Stream and Details - HEMANTH

The following **required** functionality is completed:

* [ ] The app includes functionality for **users to search and select places** (OK)
  * [ ] User can enter place names (Type Ahead) and app will fetch place names from **parse** using Restful Services (OK)
  * [ ] The app includes functionality for users to use **Location services** to determine place name (OK)
* [ ] The app includes functionality for users to perform filters on search results through in a dialog fragment.
  * [ ] User can **select filters** such as travel days / length of stay , seasons , months ,group type (Single, Family) (OK)
* [ ] User can scroll through **travel plans at a particular location** . (OK)
  * [ ] Fetch Plan details from "Parse" based on **User Details** .If no personal plans exist for user than display List of existing plans (OK)
  * [ ] For each plan displayed, user can see the following details: **Graphic, Caption, Username, Ratings, Time taken, favorite’s icon** (OK)
* [ ] For each plan displayed, user can select the plan to see following details:
  * [ ] Horizontal Scrolling **List of days** & **option to follow the plan** & **option to share the plan**
  * [ ] On click of each date we display **place name, time taken to visit, photos, created by user details and ratings**
* [ ] Display each user profile image using a **RoundedImageView**.

** Rafael Camargo **
* [ ] Plan creation: Plan Name, Created User, Time Taken(duration), Picture (places), Single/Group (Travel Type), Month (January, February). (OK)
      * [ ] User click on a button will show a dialog to create a Day plan: **place name, time taken to visit, photos, created by user details and descriptions (comments)** (OK)



The following **optional** functionality is completed:

* [ ] For plans that are added to user personal list:
  * [ ] He/She Can Update **(Add/Remove/Edit) places or details or photos**
  * [ ] Publish his new plan to **Parse**
  * [ ] **Add friends to plan**
  * [ ] View his published plan **reviews and ratings** by other users
* [ ] User can **pull-to-refresh** popular stream to get the latest popular plans
* [ ] Show **latest comments** for each plan and place in **modal overlay**
* [ ] Allow images/video posts to be played in full-screen using the **VideoView**
* [ ] Show Places on **Map**
* [ ] If filters exist then save them to **Shared Preferences** and use them for future searches
* [ ] On click of each date we display **video , weather details**
* 
**bonus**

* [] Pull a public from google image search to plan creation.

## Wireframes

![Wireframe](Wireframes/Login_1.png)
![Wireframe](Wireframes/Login_2.png)
![Wireframe](Wireframes/SignUp.png)
![Wireframe](Wireframes/Profile.png)
![Wireframe](Wireframes/HomeScreen.png)
![Wireframe](Wireframes/Detailed_Plan.png)

## Video Walkthrough 

Here's a walkthrough of implemented user stories:

<img src='' title='Video Walkthrough' width='' alt='Video Walkthrough' />

http://i.imgur.com/TulmAn2.gifv

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Unable to get Refresh on Scroll Working

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
    
