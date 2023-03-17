import React from 'react'
import {FloatingLabel, Form, Row, Col, Button} from 'react-bootstrap';

interface Props {
    description: string;
    setDescription: React.Dispatch<React.SetStateAction<string>>;

    onButtonClick: Function;
}

const HomeForm: React.FC<Props> = ({description, setDescription, onButtonClick}) => {
    return (
        // <Form>
        //     <Form.Group className="mb-3" controlId="formBasicDescription">
        //         <Form.Label>Description</HomeForm.Label>
        //         <Form.Control type="text" placeholder="Enter Description" value={description} onChange={(event)=>setDescription(event.target.value)}/>
        //     </Form.Group>
        //     <Button variant="primary" type="submit" onClick={()=>onButtonClick()}>
        //         Submit
        //     </Button>
        // </Form>


        <Form>
            <FloatingLabel
                controlId="floatingTextarea"
                label="Description"
                className="mb-3"
            >
                <Form.Control type="text" placeholder="Enter Description" value={description} onChange={(event)=>setDescription(event.target.value)}/>
                <Button variant="primary" type="submit" onClick={(e)=>onButtonClick(e)}>
                    Submit
                </Button>
            </FloatingLabel>
        </Form>



    )
}

export default HomeForm