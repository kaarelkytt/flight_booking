import "../styles/Pagination.css";
import ReactPaginate from "react-paginate";
import { useEffect, useState } from "react";
import { fetchFlights } from "../api";
import { FlightPage } from "../types";
import Filters from "./Filters";
import FlightRow from "./FlightRow";

export default function FlightList({ onSelect }: { onSelect: (id: number) => void }) {
    const [flightPage, setFlightPage] = useState<FlightPage | null>(null);
    const [currentPage, setCurrentPage] = useState(0);
    const [query, setQuery] = useState("");

    useEffect(() => {
        fetchFlights(`?page=${currentPage}&size=10${query}`)
            .then(setFlightPage)
            .catch(err => console.error(err));
    }, [currentPage]);

    return (
        <div>
            <Filters onSearch={setQuery} />

            {flightPage?.content.map((flight, index) => (
                <FlightRow
                    key={flight.id}
                    index={index + 1}
                    flightNumber={flight.flightNumber}
                    departureCity={flight.departureCity}
                    destinationCity={flight.destinationCity}
                    departureTime={flight.departureTime}
                    arrivalTime={flight.arrivalTime}
                    durationMinutes={flight.durationMinutes}
                    initialPrice={flight.initialPrice}
                    onSelect={() => onSelect(flight.id)}
                />
            ))}

            <ReactPaginate
                previousLabel={"Eelmine"}
                nextLabel={"Järgmine"}
                breakLabel={"..."}
                pageCount={flightPage?.totalPages || 1}
                marginPagesDisplayed={2}
                pageRangeDisplayed={3}
                onPageChange={(selected) => setCurrentPage(selected.selected)}
                containerClassName={"pagination"}
                activeClassName={"active"}
            />
        </div>
    );
}