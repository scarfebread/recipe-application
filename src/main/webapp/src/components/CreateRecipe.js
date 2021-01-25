import React, {useState} from 'react';
import ApiClient from "../utility/ApiClient";
import useModal from "../utility/Modal";

const CreateRecipe = (props) => {
    const [recipeName, setRecipeName] = useState('');
    const [createdRecipe, setCreatedRecipe] = useState();
    const [error, setError] = useState();
    const [modal, shouldShowModal, showModal, hideModal] = useModal(() => {setError(null)});
    let submitted = false;

    const createRecipe = () => {
        if (submitted) {
            return false;
        }

        submitted = true;
        setError(null);

        if (recipeName.length === 0) {
            setError('Invalid recipe name!');
            return false;
        }

        const success = (recipe) => {
            setCreatedRecipe(recipe);
            submitted = false;
            props.addRecipe(recipe);
        }

        const failure = (error) => {
            setError(error);
            submitted = false;
        }

        ApiClient.post('/api/recipe', {title: recipeName}, success, failure);
    }

    const renderModal = () => (
        <div ref={modal} className="modal">
            <div className="modal-content">
                <span className="close modalClose" onClick={() => hideModal()}>&times;</span>
                {!createdRecipe &&
                    <div>
                        <label htmlFor="recipeName">Enter the recipe title</label><br/>
                        <input
                            className="modalInput"
                            value={recipeName}
                            onChange={event => setRecipeName(event.target.value)}
                            onKeyUp={(event) => {
                                if (event.code === 'Enter') {
                                    createRecipe();
                                }
                            }}
                        />
                        {error && <label className="error">{error}</label>}
                        <button
                            className="button"
                            onClick={createRecipe}
                            style={{marginTop: 5}}
                        >
                            <span>CREATE</span>
                        </button>
                    </div>
                }
                {createdRecipe &&
                    <div>
                        <label>Recipe successfully created!</label><br/>
                        <label>Click <a href={'/recipe?id=' + createdRecipe.id}>here</a> to view the recipe</label>
                    </div>
                }
            </div>
        </div>
    );

    return (
        <>
            <button className="button" onClick={() => showModal()}>
                CREATE NEW RECIPE
            </button>
            {shouldShowModal && renderModal()}
        </>
    );
}

export default CreateRecipe;
