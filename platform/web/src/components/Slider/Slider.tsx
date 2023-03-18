
import Carousel from 'react-bootstrap/Carousel';
import dubai from '../../assets/dubai_pic.jpg';
import italy from '../../assets/italy_pic.jpg';
import swiss from '../../assets/swiss_pic.jpg';


export default function Slider() {
    return (
        <Carousel indicators={false} controls={false}>
            <Carousel.Item>
                <img
                    className="d-block w-100 vh-100 background"
                    src={dubai}
                    alt="First slide"
                />

            </Carousel.Item>
            <Carousel.Item>
                <img
                    className="d-block w-100 background"
                    src={italy}
                    alt="Second slide"
                />
            </Carousel.Item>
            <Carousel.Item>
                <img
                    className="d-block w-100 background"
                    src={swiss}
                    alt="Third slide"
                />
            </Carousel.Item>
        </Carousel>
    )
}
