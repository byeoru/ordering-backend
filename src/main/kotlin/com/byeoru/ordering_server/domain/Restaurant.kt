package com.byeoru.ordering_server.domain

import com.byeoru.ordering_server.domain.member.Owner
import org.locationtech.jts.geom.Point
import java.util.ArrayList
import javax.persistence.*

@Entity
class Restaurant(restaurantName: String,
                 ownerName: String,
                 address: String,
                 location: Point,
                 tableCount: Int,
                 foodCategory: FoodCategory,
                 restaurantType: RestaurantType,
                 orderingWaitingTime: Int,
                 admissionWaitingTime: Int) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
     var id: Long? = null
         protected set

     var restaurantName: String = restaurantName
         protected set

     var ownerName: String = ownerName
         protected set

    @OneToOne(mappedBy = "restaurant", fetch = FetchType.LAZY)
     var owner: Owner? = null
         protected set

    @OneToMany(mappedBy = "restaurant", orphanRemoval = true)
     var foods: MutableList<Food> = ArrayList<Food>()
         protected set

    @OneToMany(mappedBy = "restaurant")
     var orders: MutableList<Order> = ArrayList<Order>()
         protected set

    @OneToMany(mappedBy = "restaurant", cascade = [CascadeType.REMOVE], orphanRemoval = true)
     var representativeMenus: MutableList<RepresentativeMenu> = ArrayList<RepresentativeMenu>()
         protected set

    @OneToMany(mappedBy = "restaurant", cascade = [CascadeType.REMOVE], orphanRemoval = true)
     var reviews: MutableList<Review> = ArrayList<Review>()
         protected set

    @OneToMany(mappedBy = "restaurant", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    val bookmarks: List<Bookmark> = ArrayList<Bookmark>()

    @Column(name = "restaurant_address")
     var address: String = address
         protected set

     var location: Point = location
         protected set

     var tableCount: Int? = tableCount
         protected set

    @Version
     var waitingCount: Int = 0
         protected set

     var orderCount = 0
         protected set

    @Enumerated(value = EnumType.STRING)
     var foodCategory: FoodCategory = foodCategory
         protected set

    @Enumerated(value = EnumType.STRING)
     var restaurantType: RestaurantType = restaurantType
         protected set

     var profileImageUrl: String? = null
         protected set

     var backgroundImageUrl: String? = null
         protected set

     var notice: String? = null
         protected set

     var orderingWaitingTime: Int = orderingWaitingTime
         protected set

     var admissionWaitingTime: Int = admissionWaitingTime
         protected set

    fun addFood(food: Food) {
        foods.add(food)
        food.registerRestaurant(this)
    }

    fun putRestaurant(
        restaurantName: String, ownerName: String, address: String, location: Point,
        tableCount: Int, foodCategory: FoodCategory, restaurantType: RestaurantType
    ) {
        this.restaurantName = restaurantName
        this.ownerName = ownerName
        this.address = address
        this.location = location
        this.tableCount = tableCount
        this.foodCategory = foodCategory
        this.restaurantType = restaurantType
    }

    fun putProfileImageUrl(profileImageUrl: String?) {
        this.profileImageUrl = profileImageUrl
    }

    fun putBackgroundImageUrl(backgroundImageUrl: String?) {
        this.backgroundImageUrl = backgroundImageUrl
    }

    fun putOrderWaitingTime(minutes: Int) {
        orderingWaitingTime = minutes
    }

    fun putAdmissionWaitingTime(minutes: Int) {
        admissionWaitingTime = minutes
    }

    fun addRepresentativeMenu(menu: RepresentativeMenu) {
        representativeMenus.add(menu)
        menu.registerRestaurant(this)
    }

    fun registerReview(review: Review) {
        reviews.add(review)
    }

    fun increaseWaitingCount() {
        waitingCount++
    }

    fun increaseOrderCount() {
        orderCount++
    }

    fun putNotice(notice: String?) {
        this.notice = notice
    }
}