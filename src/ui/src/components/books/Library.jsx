import React, {useEffect, useState} from 'react';
import library from "../../styles/library.module.scss";
import Book from "./Book";
import CreateForm from "./CreateForm";

const getAllBooksUrl = 'http://localhost:8080/books/show_all';

const Library = () => {

    const [books, setBooks] = useState([]);
    const [show, setShow] = useState(false);
    const [isBookForm, setIsBookForm] = useState(false);
    const [isAuthorForm, setIsAuthorForm] = useState(false);

    useEffect(() => {
        fetch(getAllBooksUrl).then(response => response.json()).then(books => setBooks(books));
    });

    const showBookForm = () => {
        setShow(!show);
        setIsBookForm(!isBookForm);
    };

    const showAuthorForm = () => {
        setShow(!show);
        setIsAuthorForm(!isAuthorForm);
    };

    const showGenreForm = () => {
        setShow(!show);
    };

    return (
        show ?
        <CreateForm
            show={show}
            setShow={setShow}
            isBookForm={isBookForm}
            isAuthorForm={isAuthorForm}
            setIsBookForm={setIsBookForm}
            setIsAuthorForm={setIsAuthorForm}
        />
            :
        <div className={library.allBooks_container}>
            {
                books.map((book, key) => (
                    <Book
                         key = {key}
                         book = {book}
                    />
                ))
            }
            <div className={library.addBookBtn_container}>
                 <button
                     className={library.addBookBtn}
                     onClick={showBookForm}
                 >
                     Добавить книгу
                </button>
                <button
                    className={library.addAuthorBtn}
                    onClick={showAuthorForm}
                >
                    Добавить автора
                </button>
                <button
                    className={library.addGenreBtn}
                    onClick={showGenreForm}
                >
                    Добавить жанр
                </button>
            </div>
        </div>
    );
};

export default Library;