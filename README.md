
# Project  - *Travel Guide*

**Travel Guide** is an android app that allows a user to check out popular travel plans created by other travelers. The app utilizes information from travelers, Location services, photos and Trip Advisor API to display basic information and images about tourist places across the globe

Today, Travelers spend up to 3Hr’s crawling the web (Ex: Trip Advisor) in finding the places to visit/stay when planning for a trip. They do it because they want to tailor a plan to suit their needs/family needs so everyone can have a good time. What we can do better is - Create an app where users can create travel/trip plans from their recent trips which can be shared with other travels. By doing this travelers can spend less time in tailoring a plan and can spend more time on needed things. We will make phone UI look as simple as possible by displaying places on Map/ListView.(Here we will integrate with TripAdvisor/Yelp to pull data and we will use Phone’s features such as Camera, Persistence and Locations services)



Time spent: **2** hours spent in total

## User Stories

The following **required** functionality is completed:

**ArchTypes - Stream and Details**
* [ ] User can **scroll through current popular travel plans** 
* [ ] User can **search for travel plans at a particular destination and/or can use phone's location services** 
* [ ] User can **select and add any travel plan to his personal account** 
* [ ] For each **plan displayed, user can see the following details:**
 * [ ] **Graphic, Caption, Username,Ratings,Time taken,Add to favorites icon**
* [ ]  For each **plan displayed, user can select the plan to see following details**:
 * [ ] **places displayed based on days,time taken to visit,photos,videos,created by user details,option to follow the plan,add/update         the plan in his personal bucket Caption, Username,Ratings,Time taken,Reviews and Ratings from Trip Advisor /Yelp**


The following **optional** features are implemented:

* [ ] User can **pull-to-refresh** popular stream to get the latest popular photos
* [ ] Show latest comments for each photo
* [ ] Display each photo with the same style and proportions as the real Instagram
* [ ] Display each user profile image using a RoundedImageViewDisplay each user profile image using a [RoundedImageView](https://github.com/vinc3m1/RoundedImageView)
* [ ] Display a nice default placeholder graphic for each image during loading
* [ ] Improved the user interface through styling and coloring

The following **bonus** features are implemented:

* [ ] Show last 2 comments for each plan
* [ ] Allow user to view all comments for each plan  within a separate activity or dialog fragment
* [ ] Allow images/video posts to be played in full-screen using the VideoView

The following **additional** features are implemented:

* [ ] List anything else that you can get done to improve the app functionality!

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

    Copyright [2015] [name of copyright owner]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
