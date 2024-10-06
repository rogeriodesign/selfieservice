package br.com.acbr.acbrselfservice.di

import br.com.acbr.acbrselfservice.repository.authentication.AuthenticationRepository
import br.com.acbr.acbrselfservice.repository.authentication.service.AuthenticationService
import br.com.acbr.acbrselfservice.repository.cart.CartRepository
import br.com.acbr.acbrselfservice.repository.cart.service.CartService
import br.com.acbr.acbrselfservice.repository.configuration.ConfigurationRepository
import br.com.acbr.acbrselfservice.repository.order.OrderRepository
import br.com.acbr.acbrselfservice.repository.order.service.OrderService
import br.com.acbr.acbrselfservice.repository.product.ProductRepository
import br.com.acbr.acbrselfservice.repository.product.service.ProductService
import br.com.acbr.acbrselfservice.repository.profile.ProfileRepository
import br.com.acbr.acbrselfservice.repository.profile.service.MeService
import br.com.acbr.acbrselfservice.ui.cart.CartUseCase
import br.com.acbr.acbrselfservice.ui.cart.CartViewModel
import br.com.acbr.acbrselfservice.ui.checkout.resume.ResumeUseCase
import br.com.acbr.acbrselfservice.ui.checkout.resume.ResumeViewModel
import br.com.acbr.acbrselfservice.ui.home.HomeUseCase
import br.com.acbr.acbrselfservice.ui.home.HomeViewModel
import br.com.acbr.acbrselfservice.ui.product_list.MenuUseCase
import br.com.acbr.acbrselfservice.ui.product_list.MenuViewModel
import br.com.acbr.acbrselfservice.ui.product.ProductUseCase
import br.com.acbr.acbrselfservice.ui.product.ProductViewModel
import br.com.acbr.acbrselfservice.ui.splash.SplashContract
import br.com.acbr.acbrselfservice.ui.splash.SplashUseCase
import br.com.acbr.acbrselfservice.ui.splash.SplashViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object DependencyModule {
    val moduleApp = module {
        single<AuthenticationService> { AuthenticationService() }
        single<ProductService> { ProductService() }
        single<ConfigurationRepository> { ConfigurationRepository(androidContext()) }
        single<CartService> { CartService() }
        single<MeService> { MeService() }
        single<OrderService> { OrderService() }

        factory { AuthenticationRepository(get<AuthenticationService>()) }
        factory { ProductRepository(get<ProductService>()) }
        factory { CartRepository(androidContext()) }
        factory { ProfileRepository(androidContext(), get<MeService>()) }
        factory { MenuUseCase(get<ProductRepository>()) }
        factory { ProductUseCase(get<ProductRepository>(), get<CartRepository>()) }
        factory { CartUseCase(get<CartRepository>()) }
        factory { HomeUseCase(get<CartRepository>()) }
        factory { OrderRepository(get<OrderService>()) }
        factory { ResumeUseCase(get<OrderRepository>()) }

        //factory { SplashUseCase(get<ConfigurationRepository>(), get<OauthRepository>()) }
        single<SplashContract.UseCase> { SplashUseCase() }

        viewModel { MenuViewModel(get()) }
        viewModel { ProductViewModel(get()) }
        viewModel { CartViewModel(get()) }
        viewModel { HomeViewModel(get()) }
        viewModel { SplashViewModel(get()) }
        viewModel { ResumeViewModel(get()) }
    }
}