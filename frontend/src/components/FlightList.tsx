import ReactPaginate from "react-paginate";
import { useEffect, useState } from "react";
import { fetchFlights } from "../api";
import { FlightPage } from "../types";
import Filters from "./Filters";

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

            {flightPage?.content.map(flight => (
                <div key={flight.id} onClick={() => onSelect(flight.id)}>
                    {flight.departureCity} → {flight.destinationCity} ({flight.flightNumber})
                </div>
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