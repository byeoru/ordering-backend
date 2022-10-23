package com.byeoru.ordering_server.service

import com.byeoru.ordering_server.S3Service
import com.byeoru.ordering_server.domain.Food
import com.byeoru.ordering_server.domain.dto.FoodDto
import com.byeoru.ordering_server.domain.dto.request.FoodStatusDto
import com.byeoru.ordering_server.repository.FoodRepository
import com.byeoru.ordering_server.repository.RestaurantRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import javax.persistence.PersistenceException

@Service
@Transactional(readOnly = true)
class FoodService(private val s3Service: S3Service,
                  private val restaurantRepository: RestaurantRepository,
                  private val foodRepository: FoodRepository) {

    @Throws(PersistenceException::class)
    fun findFood(foodId: Long): Food {
        return foodRepository.findOne(foodId)
    }

    @Transactional
    fun registerFood(restaurantId: Long, dto: FoodDto, image: MultipartFile?): Long? {
        val restaurant = restaurantRepository.findOne(restaurantId)
        if (image != null) {
            val newImageKey = restaurantId.toString() + "food-image" + System.currentTimeMillis()
            val imageUrl: String = s3Service.upload(image, newImageKey)
            dto.imageUrl = imageUrl
        }
        val food = Food(dto.foodName, dto.price, false, dto.imageUrl, dto.menuIntro)
        restaurant.addFood(food)
        foodRepository.save(food)
        return food.id
    }

    @Transactional
    fun putFood(foodId: Long, restaurantId: Long, dto: FoodDto, image: MultipartFile?) {
        val food = foodRepository.findOne(foodId)
        if (image != null) {
            if (food.imageUrl != null) {
                val imageUrl = food.imageUrl
                val imageKey = imageUrl!!.substring(imageUrl.lastIndexOf("/") + 1)

                // 기존 이미지 삭제
                s3Service.delete(imageKey)
            }
            val newImageKey = restaurantId.toString() + "food-image" + System.currentTimeMillis()

            // 이미지 S3 저장
            val newImageUrl: String = s3Service.upload(image, newImageKey)
            dto.imageUrl = newImageUrl
            food.putFood(dto.foodName, dto.price, dto.menuIntro, dto.imageUrl)
        } else {
            food.putFood(dto.foodName, dto.price, dto.menuIntro)
        }
    }

    @Transactional
    fun changeSoldOutStatus(foodId: Long, dto: FoodStatusDto) {
        val food = foodRepository.findOne(foodId)
        food.changeSoldOutStatus(dto.soldOut)
    }

    fun getAllFood(restaurantId: Long) = foodRepository.findAll(restaurantId)

    @Transactional
    fun deleteFood(foodId: Long) {
        val food = foodRepository.findOne(foodId)
        val imageUrl = food.imageUrl
        if (imageUrl != null) {
            val imageKey = imageUrl.substring(imageUrl.lastIndexOf("/") + 1)
            // 이미지 삭제
            //s3Service.delete(imageKey);
        }
        foodRepository.remove(food)
    }

    companion object {
        private val log = LoggerFactory.getLogger(FoodService::class.java)
    }
}