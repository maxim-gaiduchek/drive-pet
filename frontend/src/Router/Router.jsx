import {createBrowserRouter} from "react-router-dom";
import {IndexPage} from "../Pages/Index/IndexPage";
import {DrivePage} from "../Pages/Index/DrivePage";
import {LoginPage} from "../Pages/Login/LoginPage";
import {RegistrationPage} from "../Pages/Login/RegistrationPage";

export const router = createBrowserRouter([
    {
        path: "/",
        element: <IndexPage/>
    },
    {
        path: "/drive",
        element: <DrivePage/>
    },
    {
        path: "/login",
        element: <LoginPage/>
    },
    {
        path: "/register",
        element: <RegistrationPage/>
    },
    /*{
        path: "/products/:id",
        element:
            <MainLayout>
                <ProductPage/>
            </MainLayout>
    },
    {
        path: "/products/new",
        element: <ProductCreationPage/>
    },
    {
        path: "/users/:id",
        element: <CustomerPage/>
    },
    {
        path: "/sellers",
        element: <SellersPage/>
    },
    {
        path: "/sellers/new",
        element: <SellerCreationPage/>
    },
    {
        path: "/sellers/:id",
        element: <SellerPage/>
    },
    {
        path: "/companies",
        element: <CompaniesPage/>
    },
    {
        path: "/companies/new",
        element: <CompanyCreationPage/>
    },
    {
        path: "/companies/:id",
        element: <CompanyPage/>
    },
    {
        path: "/settings",
        element: <FilterCategoriesPage/>
    },
    {
        path: "/settings/filter-categories/new",
        element: <FilterCategoryCreationPage/>
    },
    {
        path: "/settings/filter-categories/:id",
        element: <FilterCategoryPage/>
    },
    {
        path: "/settings/filter-categories/:filterCategoryId/filters/new",
        element: <FilterCreationPage/>
    },*/
])
