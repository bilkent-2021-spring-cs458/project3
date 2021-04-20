var map;
var markedLocations = [];

// Initialize the map
function initMap() {
    map = new google.maps.Map(document.getElementById("map"), {
        zoom: 4,
        center: { lat: 35.344, lng: 35.036 },
    });
}

// Check if number is a number and is in range [min, max]
function inrange(min, number, max) {
    return !isNaN(number) && number != "" && number >= min && number <= max;
}

// Check if lat and lng are valid coordinates
function validateCoordinates(lat, lng) {
    if (!inrange(-90, lat, 90) || !inrange(-180, lng, 180)) {
        alert(
            "Please enter numeric values between [-180, 180] for Longitude and [-90, 90] for Latitude."
        );
        return false;
    }
    return true;
}

// Returns the city data from google maps geocoding api results array
function findCityFromResults(results) {
    if (!results || results.length <= 0) {
        return false;
    }

    var cityIndex = -1;
    for (let index = 0; index < results.length; index++) {
        if (results[index].types[0] == "administrative_area_level_1") {
            cityIndex = index;
            break;
        }
    }

    if (cityIndex < 0) {
        for (let index = 0; index < results.length; index++) {
            if (results[index].types[0] == "administrative_area_level_2") {
                cityIndex = index;
                break;
            }
        }
    }

    if (cityIndex >= 0 && cityIndex < results.length) {
        return results[cityIndex];
    }
    return false;
}

// Reads the coordinates from input fields and validates them
function readCoordAndValidate() {
    var lng = document.getElementById("lngInput").value;
    var lat = document.getElementById("latInput").value;
    if (!validateCoordinates(lat, lng)) {
        return false;
    }
    return { lat: parseFloat(lat), lng: parseFloat(lng) };
}

// Shows the city of the input coordinates
function showCoord() {
    var pos = readCoordAndValidate();
    if (!pos) {
        return;
    }

    clearLocations();
    var city = "InvalidIndexError";
    const geocoder = new google.maps.Geocoder();
    geocoder.geocode({ location: pos }, (results, status) => {
        var info = document.getElementById("info");
        var error = true;
        if (status === "OK") {
            city = findCityFromResults(results);
            if (city) {
                error = false;
                city = city.address_components[0].long_name;
                info.innerHTML = 'Your city is <b id="result">' + city + "</b>";

                var myLatlng = new google.maps.LatLng(pos);
                var marker = new google.maps.Marker({
                    position: myLatlng,
                    map: map,
                });
                map.setCenter(myLatlng);
                map.setZoom(6);
                markedLocations.push(marker);
            }
        }

        if (error) {
            info.innerHTML =
                '<b id="result" style="color: red">There are no cities nearby.</b>';
        }
    });
}

// Clear the markers shown on map
function clearLocations() {
    for (const marker of markedLocations) {
        marker.setMap(null);
    }
}

// Handle browser not supporting geolocation api
function handleLocationError(browserHasGeolocation, infoWindow) {
    infoWindow.setPosition(map.getCenter());
    infoWindow.setContent(
        browserHasGeolocation
            ? "Error: The Geolocation service failed."
            : "Error: Your browser doesn't support geolocation."
    );
    infoWindow.open(map);
}

function degreesToRadians(degrees) {
    return (degrees * Math.PI) / 180;
}

// Calculate distance in kilometers between two coordinates on Earth.
function distanceInKmBetweenEarthCoordinates(lat1, lon1, lat2, lon2) {
    var earthRadiusKm = 6371;

    var dLat = degreesToRadians(lat2 - lat1);
    var dLon = degreesToRadians(lon2 - lon1);

    lat1 = degreesToRadians(lat1);
    lat2 = degreesToRadians(lat2);

    var a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.sin(dLon / 2) *
            Math.sin(dLon / 2) *
            Math.cos(lat1) *
            Math.cos(lat2);
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return earthRadiusKm * c;
}

// Shows the distance to a nearest city
function showDistanceToCity(mode) {
    clearLocations();

    var pos;
    if (navigator.geolocation && mode == "auto") {
        navigator.geolocation.getCurrentPosition(
            (position) => {
                pos = {
                    lat: position.coords.latitude,
                    lng: position.coords.longitude,
                };
                showDistanceToCityHelper(pos);
            },
            () => {
                handleLocationError(true, infoWindow);
            }
        );
    } else {
        pos = readCoordAndValidate();
        if (!pos) {
            return;
        }

        showDistanceToCityHelper(pos);
    }
}

function showDistanceToCityHelper(pos) {
    const geocoder = new google.maps.Geocoder();
    const InfoWindow = new google.maps.InfoWindow();
    var city = "";
    var info = document.getElementById("info");
    var error = true;

    geocoder.geocode({ location: pos }, (results, status) => {
        if (status === "OK") {
            map.setZoom(8);
            var marker = new google.maps.Marker({
                map: map,
                position: results[0].geometry.location,
            });
            markedLocations.push(marker);
            InfoWindow.setContent(results[0].formatted_address);

            city = findCityFromResults(results);
            if (city) {
                error = false;
                city = {
                    name: city.address_components[0].long_name,
                    location: city.geometry.location,
                };
                getCityCenter(city, pos);
            }
        }

        if (error) {
            info.innerHTML =
                '<b id="result" style="color: red">There are no cities nearby.</b>';
        }
    });
}

function getCityCenter(city, pos) {
    var lat = city.location.lat();
    var lng = city.location.lng();
    var distance = distanceInKmBetweenEarthCoordinates(
        pos.lat,
        pos.lng,
        lat,
        lng
    );

    map.setCenter(city.location);
    var marker = new google.maps.Marker({
        map: map,
        position: city.location,
    });
    markedLocations.push(marker);
    var info = document.getElementById("info");
    info.innerHTML =
        "Your distance to the closest city " +
        city.name +
        ' is <b id="result">' +
        distance +
        " km</b>.";
}
function showWorldDistance(mode) {
    clearLocations();
    var info = document.getElementById("info");
    info.innerText = "";
    var pos;
    if (navigator.geolocation && mode === "auto") {
        navigator.geolocation.getCurrentPosition(
            (position) => {
                pos = {
                    lat: position.coords.latitude,
                    lng: position.coords.longitude,
                };
                showWorldDistanceHelper(pos);
            },
            () => {
                handleLocationError(true, infoWindow);
            }
        );
    } else if (mode === "manual") {
        pos = readCoordAndValidate();
        if (!pos) {
            return;
        }

        // var myLatlng = new google.maps.LatLng(pos);
        showWorldDistanceHelper(pos);
    } else {
        handleLocationError(false, infoWindow);
    }
}

function showWorldDistanceHelper(pos) {
    var marker = new google.maps.Marker({
        map: map,
        position: pos,
    });
    markedLocations.push(marker);

    var marker2 = new google.maps.Marker({
        map: map,
        position: { lat: 40.52, lng: 34.34 },
    });
    map.setCenter({ lat: 40.52, lng: 34.34 });
    markedLocations.push(marker2);

    var distance = distanceInKmBetweenEarthCoordinates(
        pos.lat,
        pos.lng,
        40.52,
        34.34
    );

    var info = document.getElementById("info");
    info.innerHTML =
        'Your distance to the world center is <b id="result">' +
        distance +
        " km</b>.";
}
