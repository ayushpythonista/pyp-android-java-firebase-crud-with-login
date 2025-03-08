(In progress)

Created Empty Activity
Added to firebase console, download google-services.json but dont click next
added google-services.json in project app folder
Tools-> firebase -> AUthentication -> custom -> add the firebase
check in app build.gradle for dependencies
tools -> firebase -> realtime database -> get started -> add

Add picaso dependency in app build.gradle for dependencies
https://github.com/square/picasso
-> implementation 'com.squareup.picasso:picasso:2.71828'
Also add the BoM for the Firebase platform
https://firebase.google.com/docs/android/learn-more
-> implementation platform('com.google.firebase:firebase-bom:29.0.3')

Then Sync now

THen click next in firebase console after download google-services.json 
then again click next
then click continue to console


In console click authentication
- click get started
- click email and enable
- we can check in users
-> then click realtime
- create db
- next then start in test modeand enable

inside android studio - app-> activity-> empty activity->AddProductActivity-> finish
app-> activity-> empty activity->EditProductActivity-> finish
app-> activity-> empty activity->LoginActivity-> finish
app-> activity-> empty activity->RegisterActivity-> finish

added mtrl_textinput_box_stroke_color in res->values-> colors

Added in themes folder 
- TextInputLayout -> https://material.io/components/text-fields/android#outlined-text-field (Dense style)
- AppModalStyle - then for item android:background create custom drawable
	- new drawable resource - name: round_drawable  - root: shape (background for bottom sheet)

WOrking on register layout (Till 31:00)

create new drawable resource file for custom prgress bar
- progress_back name
- root element - rotate

add permission in manifest file

Login Activity done

Add Course Activity

Create a new Java class ProductRVModel, using for recycler view and adding data
-> Generate Constructor, Getter and Setter => Right Click->Generate
-> class ProductRVModel implements Parcelable
-> Parcelable used as passing data from one activity to another activity

Add product activity done (Till 01:09)

Edit Product Activity

create a new icon for activity main FloatingActionButton
-> drawable-> new -> vector asset

On main activity (Till 01:27:30)
- create adapter class for recycler view as well as layout file
- layout folder-> new-> layout resource file->
	name: product_rv_item
	root: androidx.cardview.widget.CardView
	OK
- package name-> new-> java class-> ProductRVAdapter
	- create inner for ViewHolder after implemet methods on clicking public ProductRVAdapter.ViewHolder method


Create a bottom sheet layout when main activity product card click
- bottom_sheet_dialogue (Relative Layout)

Create top navigation menu for logout
- res-> new-> android resource directory
	- type and name = menu
- in menu, new -> main resource file
	- name= menu_main








