import "../styles/Filters.css";

import {ChangeEvent, useEffect, useState} from "react";

type SearchParams = {
    departure: string;
    destination: string;
    startDate: string;
    endDate: string;
    minDuration: number | "";
    maxDuration: number | "";
    minPrice: number | "";
    maxPrice: number | "";
};

export default function Filters({ onSearch }: { onSearch: (query: string) => void }) {
    const [filters, setFilters] = useState<SearchParams>({
        departure: "",
        destination: "",
        startDate: "",
        endDate: "",
        minDuration: "",
        maxDuration: "",
        minPrice: "",
        maxPrice: "",
    });
    const [sortField, setSortField] = useState("departureTime");
    const [sortOrder, setSortOrder] = useState("asc");

    useEffect(() => {
        onSearch(buildQuery());
    }, [sortField, sortOrder]);

    const handleParamChange = (e: ChangeEvent<HTMLInputElement>) => {
        setFilters({ ...filters, [e.target.name]: e.target.value });
    };

    const handleSortFieldChange = (e: ChangeEvent<HTMLSelectElement>) => {
        setSortField(e.target.value);
    };

    const handleSortOrderChange = () => {
        setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    };

    const buildQuery = () => {
        const queryParams = Object.entries(filters)
            .filter(([_, value]) => value !== "" && value !== null)
            .map(([key, value]) => `${key}=${encodeURIComponent(value)}`)
            .join("&");

        return `&${queryParams}&sortField=${sortField}&sortOrder=${sortOrder}`;
    };

    const handleSearch = () => {
        onSearch(buildQuery());
    };

    return (
        <div className="filters-container">
            <div className="filters-grid">
                <div className="filter-group">
                    <label htmlFor="departure">Departure:</label>
                    <input type="text" id="departure" name="departure" placeholder="Enter departure city" onChange={handleParamChange} />
                </div>

                <div className="filter-group">
                    <label htmlFor="destination">Destination:</label>
                    <input type="text" id="destination" name="destination" placeholder="Enter destination city" onChange={handleParamChange} />
                </div>

                <div className="filter-group">
                    <label htmlFor="startDate">Start Date:</label>
                    <input type="date" id="startDate" name="startDate" onChange={handleParamChange} />
                </div>

                <div className="filter-group">
                    <label htmlFor="endDate">End Date:</label>
                    <input type="date" id="endDate" name="endDate" onChange={handleParamChange} />
                </div>

                <div className="filter-group">
                    <label htmlFor="minDuration">Min duration (min):</label>
                    <input type="number" id="minDuration" name="minDuration" placeholder="Min duration" onChange={handleParamChange} />
                </div>

                <div className="filter-group">
                    <label htmlFor="maxDuration">Max duration (min):</label>
                    <input type="number" id="maxDuration" name="maxDuration" placeholder="Max duration" onChange={handleParamChange} />
                </div>

                <div className="filter-group">
                    <label htmlFor="minPrice">Min price (€):</label>
                    <input type="number" id="minPrice" name="minPrice" placeholder="Min price" onChange={handleParamChange} />
                </div>

                <div className="filter-group">
                    <label htmlFor="maxPrice">Max price (€):</label>
                    <input type="number" id="maxPrice" name="maxPrice" placeholder="Max price" onChange={handleParamChange} />
                </div>
            </div>

            <div className="filters-actions">
                <div className="sorting">
                    <label>Sort by: </label>
                    <select id="sortField" value={sortField} onChange={handleSortFieldChange}>
                        <option value="departureTime">Time</option>
                        <option value="initialPrice">Price</option>
                    </select>
                    <button className="sort-button" onClick={handleSortOrderChange}>
                        {sortOrder === "asc" ? "↑" : "↓"}
                    </button>
                </div>

                <button className="search-button" onClick={handleSearch}>Search</button>
            </div>
        </div>

    );
}