import React, {useEffect, useState} from 'react';
import library from "../../styles/library.module.scss";
import Book from "./Book";
import CreateForm from "./CreateForm";

const getAllBooksUrl = 'http://localhost:8080/books/show_all';
const getAllAuthorsUrl = 'http://localhost:8080/authors/show_all';
const getAllGenresUrl = 'http://localhost:8080/genres/show_all';

const Library = () => {

    const [books, setBooks] = useState([]);
    const [authors, setAuthors] = useState([]);
    const [genres, setGenres] = useState([]);
    const [show, setShow] = useState(false);
    const [isBookForm, setIsBookForm] = useState(false);
    const [isAuthorForm, setIsAuthorForm] = useState(false);
    const [isSendRequestForBooks, setIsSendRequestForBooks] = useState(false);
    const [isSendRequestForAuthors, setIsSendRequestForAuthors] = useState(false);
    const [isSendRequestForGenres, setIsSendRequestForGenres] = useState(false);

    useEffect(() => {
        fetch(getAllBooksUrl)
            .then(response => response.json())
            .then(books => setBooks(books))
            .then(setIsSendRequestForBooks(!isSendRequestForBooks));

        fetch(getAllAuthorsUrl)
            .then(response => response.json())
            .then(authors => setAuthors(authors))
            .then(setIsSendRequestForAuthors(!isSendRequestForAuthors));

        fetch(getAllGenresUrl)
            .then(response => response.json())
            .then(genres => setGenres(genres))
            .then(setIsSendRequestForGenres(!isSendRequestForGenres));

    }, []);

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
            {
                authors.length === 0 || genres.length === 0 ?
            <div className={library.attentionText}>
                Книги отсутствуют. Чтобы создать книгу - создайте автора и жанр.
            </div>
            : ""
            }
            <div className={library.addBookBtn_container}>
                 <button
                     className={library.addBookBtn}
                     onClick={showBookForm}
                     disabled={authors.length === 0 || genres.length === 0}
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