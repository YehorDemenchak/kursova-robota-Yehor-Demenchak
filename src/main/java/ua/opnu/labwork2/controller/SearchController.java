package ua.opnu.labwork2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.opnu.labwork2.dto.guest.GuestResponse;
import ua.opnu.labwork2.dto.hotel.HotelResponse;
import ua.opnu.labwork2.dto.room.RoomResponse;
import ua.opnu.labwork2.service.SearchService;

import java.util.List;

@RestController
@RequestMapping("/search")
@Tag(name = "Пошук", description = "Текстовий пошук по готелях, номерах і гостях за частковим збігом")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/hotels")
    @Operation(summary = "Пошук готелів",
            description = "Шукає готелі за частковим збігом у полях name та city (case-insensitive).")
    public ResponseEntity<List<HotelResponse>> searchHotels(@RequestParam String query) {
        return ResponseEntity.ok(searchService.searchHotels(query));
    }

    @GetMapping("/rooms")
    @Operation(summary = "Пошук номерів",
            description = "Шукає номери за частковим збігом у полях type та number.")
    public ResponseEntity<List<RoomResponse>> searchRooms(@RequestParam String query) {
        return ResponseEntity.ok(searchService.searchRooms(query));
    }

    @GetMapping("/guests")
    @Operation(summary = "Пошук гостей",
            description = "Шукає гостей за частковим збігом у полях firstName, lastName та email.")
    public ResponseEntity<List<GuestResponse>> searchGuests(@RequestParam String query) {
        return ResponseEntity.ok(searchService.searchGuests(query));
    }
}
