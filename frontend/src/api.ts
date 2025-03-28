import {Preferences, Seat} from "./types.ts";

export const fetchFlights = async (query: string) => {
    const response = await fetch(`http://localhost:8080/flights/search${query}`);
    if (!response.ok) {
        throw new Error("Error fetching flights");
    }
    return response.json();
};

export const fetchSeatMap = async (flightId: number) => {
    const response = await fetch(`http://localhost:8080/flights/${flightId}/seats`);
    if (!response.ok) {
        throw new Error("Error fetching seat map");
    }
    return response.json();
};

export const fetchRecommendedSeats = async (flightId: number, preferences: Preferences, selectedSeats: Seat[]) => {
    const params = new URLSearchParams({
        numSeats: preferences.numSeats.toString(),
        window: preferences.window.toString(),
        aisle: preferences.aisle.toString(),
        extraLegroom: preferences.extraLegroom.toString(),
        nearExit: preferences.nearExit.toString(),
        adjacent: preferences.adjacent.toString()
    });

    const response = await fetch(`http://localhost:8080/flights/${flightId}/recommend-seats?${params.toString()}`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(selectedSeats.map(s => s.id))
    });
    if (!response.ok) {
        throw new Error("Failed to fetch recommended seats");
    }
    return response.json();
};