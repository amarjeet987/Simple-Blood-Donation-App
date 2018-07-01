# Simple-Blood-Donation-App

## Description :
The app implements basic registration and authentication using firebase for signing up and logging in the user, google maps API,
as well as firebase messaging services and a cloud function for device to device notification purposes. 
Of course you need to add your own google-services.json and api keys, as well as setup the cloud function to see it in action (not that hard,
just some nodejs and a few LOC ). 

## How the app works
First of all, you signup, and then login, and whenever you need blood, you tap the "I need blood" switch. It will show you a list of nearby
users, or say donors that have registered to the app. You can notify some, or all, everyone will recieve notifications. 

![home](https://user-images.githubusercontent.com/38986305/42138775-68083b60-7da0-11e8-9494-8e3e3ca563e0.PNG)



Once you are done with 
sending the notifications, you need to see how many of them accepted your request ( which they recieved theough the notification you sent to them ). The users who accepts your request will be displayed in the donors screen. 

![search](https://user-images.githubusercontent.com/38986305/42138774-67c9a1de-7da0-11e8-8ba4-7a2b506b50fd.PNG)




You can navigate to their profilefrom the donor screen where you will get their contact number, as well as location ( where the Google maps api comes into play ). You can contact the nearest one, or maybe whoever you want as per your priorities. Plus, you can only get the location of the user if he has switched on the "Share My Location" as you can see in profile screen screenshot below. Thats it.

![profile](https://user-images.githubusercontent.com/38986305/42138776-6845aab8-7da0-11e8-816d-8ea83ef18b4e.PNG)



DONATE BLOOD, SAVE LIVES !! And happy coding.
