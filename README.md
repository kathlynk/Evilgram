# Project 3 - *Evilgram*

**Evilgram** is a photo sharing app similar to Instagram but using Parse as its backend.  The only thing evil about it is its color theme.

Time spent: **11** hours spent in total

## User Stories

The following **required** functionality is completed:

- [x] User can sign up to create a new account using Parse authentication.
- [x] User can log in and log out of his or her account.
- [x] The current signed in user is persisted across app restarts.
- [x] User can take a photo, add a caption, and post it to "Instagram".

The following **optional** features are implemented:

- [x] User sees app icon in home screen and styled bottom navigation view
- [x] Style the feed to look like the real Instagram feed. (Except for color theme!  This is intentional)
- [x] After the user submits a new post, show an indeterminate progress bar while the post is being uploaded to Parse.

The following **additional** features are implemented:

- [x] Instagram theme changed to something decidely more evil :)

## Video Walkthrough

Here's a walkthrough of implemented user stories:

### Signup, Login, Logout, User Persistence
<img src="walkthroughs/Evilgram_walkthrough1.gif" width=250><br>

### Photo Capture, Caption, Post
<img src="walkthroughs/Evilgram_walkthrough2.gif" width=500><br>



## Notes

Right now, only photos taken in landscape mode are supported.  
Will need to build a custom camera activity to get functionality I want.

## Open-source libraries used

- [Android Async HTTP](https://github.com/codepath/CPAsyncHttpClient) - Simple asynchronous HTTP requests with JSON parsing
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android

## License

    Copyright [yyyy] [name of copyright owner]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
